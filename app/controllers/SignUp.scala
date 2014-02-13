
package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._
import anorm._

object SignUp extends Controller with Secured {

    val signupForm = Form(
        mapping(
            "email"    -> nonEmptyText,
            "name"     -> text(minLength = 4),
            "password" -> tuple(
                "main"    -> text(minLength = 6),
                "confirm" -> text
            ).verifying(
                "Passwords don't match", passwords => passwords._1 == passwords._2
            ).transform(
                { case (main, confirm) => main },
                (main: String) => ("", "")
            )
        )(User.apply)(User.unapply)
    )

    val loginForm = Form(
        tuple(
            "email"    -> nonEmptyText,
            "password" -> nonEmptyText
        ) verifying ("Invalid email or password", result => result match {
            case (email, password) => User.authenticate(email, password).isDefined
        })
    )

    def login = Action { implicit request =>
        Ok(html.signup.login(loginForm))
    }

    def logout = Action {
        Redirect(routes.SignUp.login).withNewSession.flashing(
            "success" -> "You've been logged out"
        )
    }

    def authenticate = Action { implicit request =>
        loginForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.signup.login(formWithErrors)),
            user => Redirect(routes.Tasks.tasks).withSession("email" -> user._1)
        )
    }

    def signup = Action {
        Ok(html.signup.form(signupForm))
    }

    def create = Action { implicit request =>
        signupForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.signup.form(formWithErrors)),
            user => {
                User.create(user)
                Ok(html.signup.registerd(user))
            }
        )
    }
}

