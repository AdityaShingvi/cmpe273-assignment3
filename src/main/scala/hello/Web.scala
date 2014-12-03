package hello

import javax.validation.constraints.NotNull

class Web(@NotNull val wurl : String, val wlogin : String, val pass : String){
  
  private var web_Id = "p"
  @NotNull
  private var url = wurl
  @NotNull
  private var login = wlogin
  @NotNull
  private var password = pass
  
   def this()={
    this(null, null, null)
  }
  
  
  def setwebId(webId : String) : Unit = {web_Id = webId  }
  def getwebId : String = {return web_Id}
  def getUrl : String = {return url}
  def getLogin : String = {return login  }
  def getPassword : String = {return password }

}

