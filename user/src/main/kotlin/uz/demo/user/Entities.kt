package uz.demo.user

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false
)

@Entity(name = "users")
class User(
    @Column(length = 128, nullable = false) var fullName: String,
    @Column(length = 12, nullable = false) var phoneNumber: String,
    @Column(length = 256) var description: String?,
    @Column(unique = true, nullable = false) var username: String,
    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "user") var profilePicture: ProfilePicture?,
    @OneToMany(mappedBy = "follower", cascade = [CascadeType.ALL]) var followers: MutableList<Follow> = mutableListOf(),
    @OneToMany(mappedBy = "following", cascade = [CascadeType.ALL]) var following: MutableList<Follow> = mutableListOf()
) : BaseEntity()


@Entity
class ProfilePicture(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Lob var imageData: ByteArray?,
)

@Entity
class Follow(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne var follower: User,
    @ManyToOne var following: User
)
