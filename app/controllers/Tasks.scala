
package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import anorm._
import views._

object Tasks extends Controller with Secured {

    val taskForm = Form(
        mapping(
            "id"       -> ignored(NotAssigned:Pk[Long]),
            "label"    -> nonEmptyText,
            "date"     -> optional(date("yyyy/MM/dd")),
            "priority" -> optional(number),
            "memo"     -> optional(text)
        )(Task.apply)(Task.unapply)
    )
    
    def tasks = IsAuthenticated { email => _ =>
        Ok(html.tasks.index(Task.all(), taskForm))
    }

    def add = IsAuthenticated { email => _ =>
        Ok(html.tasks.add(taskForm))
    }

    def edit(id: Long) = IsAuthenticated { email => _ =>
        Task.findById(id).map { task =>
            Ok(html.tasks.edit(id, taskForm.fill(task)))
        }.getOrElse(NotFound)
    }

    def update(id: Long) = IsAuthenticated { email => implicit request =>
        taskForm.bindFromRequest.fold(
            errors => BadRequest(html.tasks.index(Task.all(), errors)),
            task => {
                Task.update(id, task)
                Redirect(routes.Tasks.tasks)
            }
        )
    }

    def create = IsAuthenticated { email => implicit request =>
        taskForm.bindFromRequest.fold(
            errors => BadRequest(html.tasks.index(Task.all(), errors)),
            task => {
                Task.create(task)
                Redirect(routes.Tasks.tasks)
            }
        )
    }

    def delete(id:Long) = IsAuthenticated { email => _ =>
        Task.delete(id)
        Redirect(routes.Tasks.tasks)
    }

}
