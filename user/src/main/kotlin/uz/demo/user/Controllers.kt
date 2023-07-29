package uz.demo.user

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(UserServiceException::class)
    fun handleException(exception: UserServiceException): ResponseEntity<*> {
        return when (exception) {
            is UserNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )
        }
    }
}


@RestController
class UserController(
    private val service: UserService
) {
    @PostMapping
    fun create(@RequestBody dto: UserDTO) {
        service.create(dto)
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): UserDTO? {
        var userDto = service.getById(id)
        return userDto
    }

    @PutMapping("{id}")
    fun updateProfile(@PathVariable id: Long, @RequestBody dto: UserUpdateDto) {
        dto.id = id
        service.updateProfile(dto)
    }

    @PostMapping("{id}/profilePicture")
    fun uploadProfilePicture(@PathVariable id: Long, @RequestParam imageData: MultipartFile) {
        val imageBytes = imageData.bytes
        service.uploadProfilePicture(id, imageBytes)
    }

    @GetMapping("{id}/followers")
    fun getFollowers(@PathVariable id: Long): ResponseEntity<List<UserDTO>> {
        val followers = service.getFollowers(id)
        return ResponseEntity.ok(followers)
    }

    @GetMapping("{id}/following")
    fun getFollowing(@PathVariable id: Long): ResponseEntity<List<UserDTO>> {
        val following = service.getFollowing(id)
        return ResponseEntity.ok(following)
    }

    @PostMapping("{followerId}/follow/{followingId}")
    fun followUser(
        @PathVariable followerId: Long,
        @PathVariable followingId: Long
    ) {
        service.followUser(followerId, followingId)
    }

    @PostMapping("{followerId}/unfollow/{followingId}")
    fun unfollowUser(
        @PathVariable followerId: Long,
        @PathVariable followingId: Long
    ) {
        service.unfollowUser(followerId, followingId)
    }
}


//@RestController
//@RequestMapping("internal")
//class UserInternalController(private val service: UserService) {
//    @GetMapping("exists/{id}")
//    fun existById(@PathVariable id: Long) = service.existById(id)
//}

@RestController
@RequestMapping("internal")
class UserInternalController(private val service: UserService) {
    @GetMapping("exists/{id}")
    fun existById(@PathVariable id: Long): ResponseEntity<Boolean> {
        val exists = service.existById(id)
        return ResponseEntity.ok(exists)
    }
}
