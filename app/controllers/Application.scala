package controllers
import org.joda.time.LocalDateTime
import play.api._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.mvc._
import views._
import models.Knolder
import models.knolTable

object Application extends Controller {

  /**
   * ********************************************
   * Home redirects to the default page showEmp *
   * ********************************************
   */
  val Home = Redirect(routes.Application.index)

  /* Mapping knolder form
   */
  val knolForm = Form(
    mapping(
      "id" -> ignored(Some(0): Option[Int]),
      "name" -> text,
      "email" -> nonEmptyText,
      "mobile" -> nonEmptyText)(Knolder.apply)(Knolder.unapply))

  /**
   * *********************************************************************
   * Handles the default action, renders list of employees from database *
   * *********************************************************************
   */

  def index: Action[AnyContent] = DBAction { implicit rs =>
    Ok(views.html.index(knolTable.getKnolderList()))

  }
  /**
   * *****************************************
   * Display the 'new Knolder form'.        *
   * *****************************************
   */
  def create = Action {
    Ok(html.createForm(knolForm))
  }

  /**
   * *****************************************
   * Display the 'edit Knolder Form'.        *
   * *****************************************
   * @param id of the Knolder to edit
   */
  def edit(knolId: Int) = DBAction { implicit request =>
     knolTable.getKnolderById(knolId) match {
      case knolder => Ok(html.editForm(knolId, knolForm.fill(knolder)))
    }
  }
  /**
   * *******************************************
   * Handles the submit of 'new Knolder form' *
   * *******************************************
   */
  def save = DBAction { implicit request =>
    knolForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createForm(formWithErrors)),
      knolder => {
        knolTable.insertKnolder(knolder)
        val msg = "Company ${emp.name} has been created"
        Home.flashing("success" -> msg)
      })
  }
  /**
   * ****************************************
   * Handles the delete link of the record  *
   * ****************************************
   */
  def deleteKnol(id: Int) = DBAction { implicit request =>
    knolTable.deleteKnolder(id)
    Home.flashing("success" -> "Emp has been deleted")
  }
  
  /**
   * Handle the 'edit form' submission
   *
   * @param id Id of the Knolder to edit
   */
  def updateKnolder(knolderId: Int) = DBAction { implicit request =>
    knolForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.editForm(knolderId, formWithErrors)),
      knolder => {
        val knolderToUpdate: Knolder = knolder.copy(Some(knolderId))
        knolTable.updateKnolder(knolderToUpdate)
        Home.flashing("success" -> s"Company ${knolder.name} has been updated")
      })
  }
}

