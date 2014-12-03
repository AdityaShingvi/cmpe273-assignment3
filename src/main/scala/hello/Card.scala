package hello

import javax.validation.constraints.NotNull

class Card(val cardna : String, val c_number : String, val exp_date : String){
  
  private var Id = "p"
  @NotNull
  private var card_name = cardna
  @NotNull
  private var card_number = c_number
  private var expiration_date = exp_date
  
   def this()={
    this(null,null,null)
  }
  
  
  def setcardId(cardId : String) : Unit = {Id = cardId  }
  def setCard_number(cardNumber : String) : Unit = {card_number = cardNumber  }
  def getcardId : String = {return Id}
  def getCard_number : String = {return card_number}
  def getExpiration_date : String = {return expiration_date  }
  def getCard_name : String = {return card_name }

}