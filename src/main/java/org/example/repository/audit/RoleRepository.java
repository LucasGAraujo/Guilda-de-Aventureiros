package org.example.repository.audit;

import org.example.domain.audit.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.organizacao.id = :organizacaoId")
    List<Role> findAllWithPermissionsByOrganizacaoId(@Param("organizacaoId") Long organizacaoId);
}

