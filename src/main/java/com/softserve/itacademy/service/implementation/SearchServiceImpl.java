package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.projection.CourseTinyProjection;
import com.softserve.itacademy.projection.GroupTinyProjection;
import com.softserve.itacademy.service.SearchService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class    SearchServiceImpl implements SearchService {

    private final EntityManager entityManager;
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    public SearchServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<GroupTinyProjection> searchGroup(String name) {

        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> root = criteriaQuery.from(Group.class);

        criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), "%" + name + "%"));

        Query<Group> query = session.createQuery(criteriaQuery);
        List<Group> results = query.getResultList();
        return results.stream().map((group) -> projectionFactory.createProjection(GroupTinyProjection.class, group)).collect(Collectors.toList());
    }

    @Override
    public List<CourseTinyProjection> searchCourse(String searchQuery) {

        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);

        Predicate predicateOnName = criteriaBuilder.like(root.get("name"), "%" + searchQuery + "%");
        Predicate predicateOnDescription = criteriaBuilder.like(root.get("description"), "%" + searchQuery + "%");
        Predicate predicate = criteriaBuilder.or(predicateOnDescription, predicateOnName);
        criteriaQuery.select(root).where(predicate);

        Query<Course> query = session.createQuery(criteriaQuery);
        List<Course> results = query.getResultList();
        return results.stream().map((course) -> projectionFactory.createProjection(CourseTinyProjection.class, course)).collect(Collectors.toList());
    }

}
