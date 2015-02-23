package controllers
import models._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.Play.current
import views._

object Application extends Controller {

  /* Mapping knolder form
   */
  
  
  def addemployee = Action {
    Ok(views.html.addemployee("addemployee"))
  }
  def getKnolderController = Action {  
    Ok(views.html.viewknolder("Render Emp"))    
  }
  def index: Action[AnyContent]= DBAction { implicit rs => 
    Ok(views.html.index(knolTable.getKnolderList()))
    
  }
  
  
}