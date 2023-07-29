package uz.demo.user

data class BaseMessage(var code: Int? = null, var message: String? = null)

data class UserDTO(

    val id: Long?,
    val fullName: String,
    val phoneNumber: String,
    val username: String,
    val description: String?,
    val followersCount: Int,
    val followingCount: Int,
    val profilePicture: ProfilePictureDTO? = null
) {
        fun toEntity() =  User(fullName, username, phoneNumber,username)

    private fun User(fullName: String, phoneNumber: String, description: String, username: String): User {
        TODO("Not yet implemented")
    }


    data class GetUserDto(
            val id: Long?,
            val fullName: String,
            val username: String,
            val bio: String
        ){
        companion object {
            fun toDto(user: User) = user.run { GetUserDto(id, fullName, username, description!!) }

        }
    }

    companion object{
        fun toDto(entity: User) = entity.run {
            UserDTO(
                id,
                fullName,
                phoneNumber,
                username,
                description,
                followersCount = this.followers.size,
                followingCount = this.following.size
            )
        }
    }
}




data class ProfilePictureDTO(
    val id: Long?,
    val imageData: ByteArray?
)


data class UserUpdateDto(
    var id: Long,
    val fullName: String,
    val phoneNumber: String,
    val description: String?
)
data class FollowDto(
    val followerId: Long,
    val followingId: Long
)
