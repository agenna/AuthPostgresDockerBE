package com.agenna.authPostgres.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.agenna.authPostgres.entity.User;


@RepositoryRestResource(collectionResourceRel = "user", path="users")
public interface UserRepository extends JpaRepository<User, Long> {

    // select * from user u where u.email = :email
    public Optional<User> findByEmail(String email);

    // select * from user u where u.id = :id
    public Optional<User> findById(Long id);

/*     @Query(" select u.* from user u inner join token t on u.id = t.user where u.id= :id and t.refresh_token = :refreshToken and t.expired_at >= :expiredAt")
    Optional<User> findByIdAndTokensRefreshTokenAndExpiredAtGreaterThan
                        (Long id, String refreshToken, LocalDateTime expiredAt);
 */
/*     @Query(" select u.* from user u inner join token t on u.id = t.user_id where u.id= :id and t.refresh_token = :refreshToken ")
    Optional<User> findByIdAndTokensRefreshToken
                   (Long id, String refreshToken);
*/

/*     @Query(" select * from user u inner join password_recovery p on u.id = p.user_id where u.id= :id and t.token = :token ")
    Optional<User> findByIdAndToken
                   (Long id, String token); */
}