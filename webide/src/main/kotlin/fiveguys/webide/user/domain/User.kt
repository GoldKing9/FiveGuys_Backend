package fiveguys.webide.user.domain

import jakarta.persistence.*

@Entity
class User(
    val email: String,
    val password: String,
    val nickname: String,
    @Enumerated(EnumType.STRING)
    val role: UserRole,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

}
