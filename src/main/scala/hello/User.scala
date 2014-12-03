package hello

import scala.beans._
import java.util.concurrent.atomic.AtomicLong
import javax.validation.constraints.NotNull

object User {

}

class User(val email1 : String, @NotNull val pass: String) {

  private var UId : String = null;
  @NotNull
  private var email : String  = email1  
  @NotNull
  private var password: String = pass
  var map_card : Map[String, Card] = Map()
  var map_web : Map[String, Web] = Map()
  var map_bank : Map[String, Bank] = Map()
  private var cnt_card = 0
  private var cnt_bank = 0
  private var cnt_web = 0
  var card = new Card("testName", "333", "12.324.5")
  var web = new Web("testName", "333", "12.324.5")
  var bank = new Bank("testName", "333", "12.324.5")
  private var created_at : String = null
  def this()={
    this(null,null)
  }
  
  def getUserId : String = (return UId)
  def getPassword : String = { return password}
  def getEmail : String = { return this.email}
  def getcreated_at : String = {return created_at}
  def setcreated_at(currenttime : String) : Unit = {created_at = currenttime}
  def setEmail(email : String) : Unit = {this.email = email}
  def setPassword(pass : String) : Unit = {this.password = pass}
  def setUserId (Id : String): Unit = {UId = Id}
  def makecardmap (usercard : Card) : Card = {
    this.card = usercard
	card.setCard_number(usercard.getCard_number)
    this.cnt_card += 1
    var cardId = "c-" + this.cnt_card.toString()
    card.setcardId(cardId)
    this.map_card = map_card + (cardId -> card)
    return this.card
  }
  
  def makewebmap (userweb : Web) : Web = {
    web = userweb
    this.cnt_web += 1
    var webId = "l-" + cnt_web.toString()
    web.setwebId(webId)
    map_web = map_web + (webId -> web)
    return web
  }
  
  def makebankmap (userbank : Bank) : Bank = {
    bank = userbank
    this.cnt_bank += 1
    var bankId = "b-" + cnt_bank.toString()
    bank.setbankId(bankId)
    map_bank = map_bank + (bankId -> bank)
    return bank
  }
}