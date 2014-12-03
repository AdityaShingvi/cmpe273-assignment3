package hello
import javax.validation.constraints.NotNull
class Bank(val aname : String, val rnum : String, val anum : String){
  
  private var Id = "p"
  @NotNull
  private var account_name = aname
  @NotNull
  private var routing_number = rnum
  @NotNull
  private var account_number = anum
  
   def this()={
    this(null,null,null)
  }
  
  
  def setbankId(cardId : String) : Unit = {Id = cardId  }
  def getcardId : String = {return Id}
  def getAccount_name : String = {return account_name}
  def getRouting_number : String = {return routing_number  }
  def getAccount_number : String = {return account_number }

}