package ru.practicum.explore.ewm.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.explore.ewm.user.dto.UserDto;

import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT new ru.practicum.explore.ewm.user.dto.UserDto(u.id, u.email, u.name) " +
            "FROM User AS u " +
            "WHERE u.id IN :ids")
    List<UserDto> findDesiredUsers(Set<Integer> ids, Pageable pageable);

    @Query("SELECT new ru.practicum.explore.ewm.user.dto.UserDto(u.id, u.email, u.name) " +
            "FROM User AS u")
    List<UserDto> findAllUsers(Pageable pageable);

    User save(User user);

    void deleteById(int userId);

    boolean existsById(int userId);

    User getReferenceById(int userId);
}
