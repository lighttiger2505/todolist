package controllers

import play.api._
import play.api.mvc._

trait Secured {
    private def email(request: RequestHeader) = 
       request.session.get("email")

    private def onUnauthorized(request: RequestHeader) =
        Results.Redirect(routes.Application.index)

    def IsAuthenticated(f: => String => Request[AnyContent] => Result) = 
        Security.Authenticated(email, onUnauthorized) { user =>
            Action(request => f(user)(request))
        }
}
