package com.example.allergydetective.data.model.user

// 유저 관리
data class User(val id: String,
                val pw: String,
                val name: String,
                val contact: String,
                val email: String,
                val allergy: List<Boolean> = emptyList(),
                val group: List<GroupMember> = emptyList(),
                val membership: String = "",
                val like: List<String> = emptyList(),
                val mypost: List<Post> = emptyList(),
                val scrap: List<Post> = emptyList()) {
}

object UserManager{
    val users = mutableListOf<User>()

    fun delete(id_: String){
        users.remove(users.find { it.id == id_ })
    }
}

// 유저 - 그룹 관리
data class GroupMember(val name: String, val mixFilter: Boolean, val allergy: List<Boolean>){
}
object GroupManager{
    val members = mutableListOf<GroupMember>()

    fun add(name: String, mixFilter: Boolean, allergy: List<Boolean>) {
        members.add(GroupMember(name, mixFilter, allergy))
    }

    fun delete(name_: String){
        members.remove(members.find { it.name == name_ })
    }
}



// 게시글 관리
data class Post(val index: Int, val subject: String, val poster: String, val title: String, val content: String,
            val comment: Int, val report: Boolean){
}
object PostManager{

    val posts = mutableListOf<Post>()

    fun add(index: Int, subject: String, poster: String, title: String, content: String,
            comment: Int, report: Boolean) {
        posts.add(Post(index, subject, poster, title, content, comment, report))
    }

    fun delete(index_: Int){
        posts.remove(posts.find { it.index == index_ })
    }

}



