package models
import play.api.db.slick.Config.driver.simple._
import scala.collection.immutable.List

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

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}    

//Initializing the TableQuery object 
object knolTable {
  val knolTableQuery = TableQuery[KnolderTable]
  def insert(knolder: Knolder)(implicit s: Session): Int = {
    knolTableQuery.insert(knolder)
  }

  /**
   * ************************************************************************************
   * fetches the list of Knolder records from the database                              *
   * ************************************************************************************
   * @param : currentPage
   * @param : pageSize
   */
  def getKnolderList(currentPage:Int ,pageSize:Int)(implicit session: Session): List[Knolder] = {
    knolTableQuery.list.drop(currentPage*pageSize).take(pageSize)
  }
  /**
   * *********************************
   * Returns the count of knolders   *
   * *********************************
   */
  
   def getKnolCount()(implicit session: Session): Int = {
    knolTableQuery.list.length
  }
  
  /**
   * ************************************************************************************
   * Inserts the  knolder record into the database                                      *
   * ************************************************************************************
   * @param: knolder record to be deleted
   */
  def insertKnolder(knolder: Knolder)(implicit session: Session) = {
    knolTable.insert(knolder)
  }
  /**
   * ************************************************************************************
   * Deletes the  Knolder record from the database table                                *
   * ************************************************************************************
   */
  def deleteKnolder(knolderId: Int)(implicit session: Session) = { 
    knolTableQuery.filter(_.id === knolderId).delete
  }
  /**
   * ************************************************************************************
   * Updates the  Knolder record from the database table                                *
   * ************************************************************************************
   * @param: Knolder Record
   */
  
 def updateKnolder(knolder: Knolder)(implicit session: Session) ={
  val knolList = knolTableQuery.filter {x => x.id === knolder.id }.update(knolder)
 }
  
  /**
   * ************************************************************************************
   * Returns the  Knolder record by id from the database table                                *
   * ************************************************************************************
   * @param: Id of the knolder
   */
  
  def getKnolderById(knolderId:Int)(implicit session: Session):Knolder ={
    val knolList = knolTableQuery.filter { x => x.id === knolderId }.list
    knolList.head
  }
  
  /**
   * Return a page of Knolders
   * @param page
   * @param pageSize
   * @param orderBy
   * @param filter
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%")(implicit s: Session): Page[Knolder] = {
    val offset = pageSize * page
    val query =
      (for {
        knolder <- knolTableQuery
        if knolder.name.toLowerCase like filter.toLowerCase()
      } yield (knolder)).drop(offset).take(pageSize)
    val totalRows = getKnolCount()
    val result = query.list
    Page(result, page, offset, totalRows)
  }
} 

