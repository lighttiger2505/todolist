package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(
    id: Pk[Long], 
    label: String,
    memo: String
)

object Task {

    val simple = {
        get[Pk[Long]]("id") ~
        get[String]("label") ~
        get[String]("memo") map {
            case id~label~memo => Task(id, label, memo)
        }
    }

    def all(): List[Task] = DB.withConnection { implicit c =>
        SQL("select * from task").as(Task.simple *)
    }

    def findById(id: Long): Option[Task] = {
        DB.withConnection { implicit c =>
            SQL("select * from task where id = {id}").on(
                'id -> id
            ).as(Task.simple.singleOpt)
            }
        }

    def create(task: Task) {
        DB.withConnection { implicit c =>
            SQL("insert into task (label, memo) values ({label}, {memo})").on(
                'label -> task.label,
                'memo -> task.memo
            ).executeUpdate()
            }
        }

    def update(id: Long, task: Task) = {
        DB.withConnection { implicit c =>
            SQL("update task set label = {label} where id = {id}").on(
                'id -> id,
                'label -> task.label,
                'memo -> task.memo
            ).executeUpdate()
        }
    }

    def delete(id: Long) {
        DB.withConnection { implicit c =>
            SQL("delete from task where id = {id}").on(
                'id -> id
            ).executeUpdate()
            }
        }
    }
