package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._

import anorm._

object Application extends Controller {

    val taskForm = Form(
        mapping(
            "id" -> ignored(NotAssigned:Pk[Long]),
            "label" -> nonEmptyText,
            "date" -> optional(date("yyyy/MM/dd")),
            "priority" -> optional(number),
            "memo" -> optional(text)
        )(Task.apply)(Task.unapply)
    )

    def index = Action {
        Redirect(routes.Application.tasks)
    }

    def tasks = Action {
       Ok(views.html.index(Task.all(), taskForm))
    }

    def add = Action {
        Ok(views.html.add(taskForm))
    }

    def edit(id: Long) = Action {
        Task.findById(id).map { task =>
            Ok(views.html.edit(id, taskForm.fill(task)))
            }.getOrElse(NotFound)
        }

        def update(id: Long) = Action { implicit request =>
            taskForm.bindFromRequest.fold(
                errors => BadRequest(views.html.index(Task.all(), errors)),
                task => {
                    Task.update(id, task)
                    Redirect(routes.Application.tasks)
                }
                )
        }

        def create = Action { implicit request =>
            taskForm.bindFromRequest.fold(
                errors => BadRequest(views.html.index(Task.all(), errors)),
                task => {
                    Task.create(task)
                    Redirect(routes.Application.tasks)
                }
                )
        }

        def delete(id:Long) = Action {
            Task.delete(id)
            Redirect(routes.Application.tasks)
        }

    }

