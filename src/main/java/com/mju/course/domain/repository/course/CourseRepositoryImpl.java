package com.mju.course.domain.repository.course;

import com.mju.course.domain.model.Course;
import com.querydsl.core.types.Projections;

import java.util.ArrayList;
import com.mju.course.domain.model.QCourse;
import com.mju.course.domain.model.QSkill;
import com.mju.course.domain.model.enums.CourseState;
import com.mju.course.presentation.dto.response.CoursesReadDto;
import com.mju.course.presentation.dto.response.admin.AdminReadCoursesDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class CourseRepositoryImpl implements CourseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CourseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CoursesReadDto> readCourseList(String category, String order, List<String> skillList, Pageable pageable) {
        QCourse course = QCourse.course;
        QSkill skill = QSkill.skill1;

        // Query 객체 생성
        JPQLQuery<CoursesReadDto> query = queryFactory
                .selectDistinct(Projections.constructor(CoursesReadDto.class,
                        course.id,
                        course.category,
                        course.courseName,
                        course.price,
                        course.difficulty,
                        course.courseTitlePhotoKey))
                .from(course)
                .leftJoin(course.skillList, skill)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // if category가 존재한다면
        if(category != null && !category.isEmpty()){
            BooleanExpression categoryPredicate = course.category.in(category);
            query.where(categoryPredicate);
        }

        // if order가 존재한다면
        if(order != null && !order.isEmpty()){
            OrderSpecifier<?> orderSpecifier = getOrderByExpression(order, course);
            query.orderBy(orderSpecifier);
        }

        // if skill이 존재한다면
        if (skillList != null && !skillList.isEmpty()) {
            BooleanExpression skillPredicate = course.skillList.any().skill.in(skillList);
            query.where(skillPredicate);
        }

        // Query 실행
        QueryResults<CoursesReadDto> results = query.fetchResults();

        List<CoursesReadDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<AdminReadCoursesDto> readAdminCourseList(String state, String order, Pageable pageable) {
        QCourse course = QCourse.course;

        // 쿼리 객체 생성
        JPQLQuery<Course> query = queryFactory.selectFrom(course)
                .leftJoin(course.skillList)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // if state가 존재한다면
        if (!"all".equals(state)) {
            try {
                CourseState courseState = CourseState.valueOf(state.toUpperCase());
                query.where(course.status.eq(courseState));
            } catch (IllegalArgumentException e) {
                // 잘못된 상태 값이 전달된 경우 처리
            }
        }

        // if order가 존재한다면
        if(order != null && !order.isEmpty()){
            OrderSpecifier<?> orderSpecifier = getOrderByExpression(order, course);
            query.orderBy(orderSpecifier);
        }

        // Query 실행
        List<Course> courseList = query.fetch();

        List<AdminReadCoursesDto> content = new ArrayList<>();
        courseList.forEach(s->{
            AdminReadCoursesDto adminReadCoursesDto = AdminReadCoursesDto.of(s);
            content.add(adminReadCoursesDto);
        });

        JPAQuery<Course> countQuery = queryFactory.selectFrom(course)
                .leftJoin(course.skillList)
                .fetchJoin();

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    public static OrderSpecifier<?> getOrderByExpression(String orderTarget, QCourse course) {
        PathBuilder<?> orderPath = new PathBuilder<>(course.getType(), course.getMetadata());
        return new OrderSpecifier(Order.DESC, orderPath.get(orderTarget));
    }

}