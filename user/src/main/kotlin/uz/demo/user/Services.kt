package uz.demo.user

import org.springframework.stereotype.Service
import uz.demo.user.UserDTO
import uz.demo.user.UserRepository
import uz.demo.user.UserUpdateDto


interface UserService {
    fun create(dto: UserDTO)
    fun getById(id: Long): UserDTO?
    fun existById(id: Long): Boolean
    fun updateProfile(dto: UserUpdateDto)
    fun uploadProfilePicture(userId: Long, imageData: ByteArray)
    fun getFollowers(userId: Long): List<UserDTO>
    fun getFollowing(userId: Long): List<UserDTO>
    fun followUser(followerId: Long, followingId: Long)
    fun unfollowUser(followerId: Long, followingId: Long)
}

@Service
class UserServiceImpl(
    private val repository: UserRepository) :
    UserService {

    override fun create(dto: UserDTO) {
        val username: String = dto.username
        if (repository.existsByUsername(username)) throw ExistsUsernameException()
        repository.save(dto.toEntity())
    }

    override fun getById(id: Long): UserDTO? {
         repository.findByIdAndDeletedFalse(id)?.let { UserDTO.toDto(it) }?: throw RuntimeException("Topilmadi")
    }

    override fun existById(id: Long): Boolean {
        return repository.existsByIdAndDeletedFalse(id)
    }

    override fun updateProfile(dto: UserUpdateDto) {
        val existingUser = repository.findByIdAndDeletedFalse(dto.id)

        }


    override fun uploadProfilePicture(userId: Long, imageData: ByteArray) {

    }

    override fun getFollowers(userId: Long): List<UserDTO> {
        val user = repository.findByIdAndDeletedFalse(userId)
            val followers = user!!.followers

        return repository.findUserByFollowingId(followers)
    }


override fun getFollowing(userId: Long): List<UserDTO> {
            val user = repository.findByIdAndDeletedFalse(userId)
            if (user != null) {
                val followingUsers = user.following
                return followingUsers.map { followingUser ->
                    UserDTO(
                        followingUser.id,
                        followingUser.following.fullName,
                        followingUser.following.phoneNumber,
                        followingUser.following.username,
                        followingUser.following.description,
                        followingUser.follower.followers.size,
                        followingUser.following.following.size
                    )
                }
            }
            return emptyList()
        }


        override fun followUser(followerId: Long, followingId: Long) {
            val follower = repository.findByIdAndDeletedFalse(followerId)
            val following = repository.findByIdAndDeletedFalse(followingId)

            if (!repository.existsByIdAndDeletedFalse(id = Long.MAX_VALUE)) throw UserNotFoundException()
                if (!follower.following.contains(following)) {
                    follower.following.add(following)
                    follower.followingCount++
                }
                if (!following.followers.contains(follower)) {
                    following.followers.add(follower)
                    following.followerCount++
                }
                repository.save(follower)
                repository.save(following)
            }



    override fun unfollowUser(followerId: Long, followingId: Long) {
        val follower = repository.findByIdAndDeletedFalse(followerId)
        val following = repository.findByIdAndDeletedFalse(followingId)
        if (follower != null && following != null) {
            if (follower.following.containsAll(follower)) {
                follower.following.removeAll(following)
                follower.followingCount--
            }
            if (following.followers.contains(follower)) {
                following.followers.remove(follower)
                following.followerCount--
            }
            repository.save(follower)
            repository.save(following)
        }
    }
}
