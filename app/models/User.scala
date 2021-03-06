
package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class User(
    email    : String,
    name     : String,
    password : String
)

object User {
    val simple = {
        get[String]("user.email")~
        get[String]("user.name")~
        get[String]("user.password") map {
            case email~name~password => User(email,name,password)
        }
    }

    def findAll: Seq[User] = {
        DB.withConnection { implicit connection =>
            SQL("select * from user").as(User.simple *)
        }
    }

    def authenticate(email: String, password: String): Option[User] = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                select * from user where 
                email = {email} and password = {password}
                """
                ).on(
                    'email    -> email,
                    'password -> password
                ).as(User.simple.singleOpt)
        }
    }

    def create(user: User): User = {
        DB.withConnection { implicit connection =>
            SQL(
                """
                insert into user values (
                {email}, {name}, {password}
                )
            """
            ).on(
                'email    -> user.email,
                'name     -> user.name,
                'password -> user.password
            ).executeUpdate()
            user
        }
    }
}
