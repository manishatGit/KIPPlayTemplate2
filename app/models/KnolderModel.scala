package models
import play.api.db.slick.Config.driver.simple._

/**
 * Model: Knolder contains case class Knolder and an Object with insert method
 */
case class Knolder(val id: Option[Int], val name: String, val email: String, val mobile: String)
class KnolderTable(tag: Tag) extends Table[Knolder](tag, "Knolder") {
  def id: Column[Option[Int]] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Column[String] = column[String]("name", O.NotNull)
  def email: Column[String] = column[String]("email", O.NotNull)
  def mobile: Column[String] = column[String]("mobile", O.NotNull)
  def * = (id, name, email, mobile) <> (Knolder.tupled, Knolder.unapply)
}
//Initializing the TableQuery object 
object knolTable {
  val knolTableQuery = TableQuery[KnolderTable]
  def insert(knolder: Knolder)(implicit s: Session): Int = {
    knolTable.insert(knolder)
  }
  
  /**************************************************************************************
   * fetches the list of Knolder records from the database                              *
   **************************************************************************************/
  def getKnolderList()(implicit session:Session):List[Knolder]={
    knolTableQuery.list
  }
  /**************************************************************************************
   * Inserts the  Knolder record into the database                              *
   **************************************************************************************/
  def insertKnolder(knolder: Knolder )(implicit session:Session)={
    knolTable.insert(knolder)
  }
    
  
  
} 

