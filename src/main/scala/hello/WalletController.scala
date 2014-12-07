package hello

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype._
import org.springframework.boot.autoconfigure._
import org.springframework.web.bind.annotation._
import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping, RequestParam, RestController}
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
import org.springframework.http.HttpStatus
import collection.JavaConversions._
import java.util.ArrayList
import collection.JavaConversions._
import javax.validation.Valid
import org.springframework.validation.BindingResult
import org.joda.time.DateTime
import org.springframework.http.{HttpHeaders, ResponseEntity}
import javax.ws.rs.core._
//import jetcd._
import java.net._
import com.justinsb.etcd.EtcdClientException
import com.justinsb.etcd.EtcdResult
import com.justinsb.etcd.EtcdClient

@RestController
@Configuration
@EnableAutoConfiguration
@ComponentScan
class WalletController {

  var cnt1 = 0
  var  user = new User();
  var map_usr : Map[String, User] = Map();
  val client_cnt: EtcdClient = new EtcdClient(URI.create("http://54.183.162.215:4001/"))
  
  @RequestMapping(value = Array("/api/V1/users"), method = Array(RequestMethod.POST), headers = Array("content-type=application/json"), consumes = Array("application/json"))
  def userCreation(@Valid @RequestBody user : User, result: BindingResult):User = {
    
    if (result.hasErrors()) {
      throw new ParameterMissingException(result.toString)
    } 
    else 
    {            
      cnt1 = cnt1+1
      this.user = user
 
    var str = "U-" + cnt1.toString()
    user.setUserId(str)
    val currentTime = DateTime.now;
    this.user.setcreated_at(currentTime.toString)
    this.map_usr = this.map_usr + (str -> this.user)
    this.map_usr.keys.foreach{i =>
     						print("Key " + i)
     						println("Values " + map_usr(i))
   }}
   return user
   
}
  
  @RequestMapping(value=Array("/api/V1/users/{userid}"), method=Array(RequestMethod.GET))
  def ViewUsers(@PathVariable("userid")  userId:String,@RequestHeader(value="If-None-Match", required=false) Etag: String):ResponseEntity[_]={
		var usr :User = map_usr(userId);
        var entity_tag1: String = Etag
		var cc: CacheControl = new CacheControl()
        cc.setMaxAge(500)
        var entity_tag2: EntityTag = new EntityTag(Integer.toString(usr.hashCode()));
        println(entity_tag2);
        var responseHeader: HttpHeaders = new HttpHeaders	
        responseHeader.setCacheControl(cc.toString())
        responseHeader.add("Etag", entity_tag2.getValue())
        if(entity_tag2.getValue().equalsIgnoreCase(entity_tag1)){
        	 println("Not_Modified");
                   new ResponseEntity[String]( null, responseHeader, HttpStatus.NOT_MODIFIED )   
        } else {
                println("Modified");
        		new ResponseEntity[User]( usr, responseHeader, HttpStatus.OK )  
        }
  }
  
  /*def viewuser(@PathVariable("userid")  userId:String ):User={
		var map_u1 = map_usr(userId)
       println(""+ map_u1.getEmail);
		return map_u1;
	}*/ 
  
    @RequestMapping(value = Array("/api/V1/users/{userid}"), method = Array(RequestMethod.PUT), headers = Array("content-type=application/json"), consumes = Array("application/json"))
	def upduser(@PathVariable("userid")  userId:String ,@RequestBody user : User ):User={
      var usr = new User()
      user.setUserId(userId)
      this.map_usr = this.map_usr + (userId -> user)
		usr = map_usr(userId)
		println(usr.getEmail)
      return user
		
    }
    
   @RequestMapping(value = Array("/api/V1/users/{userid}/idcards"), method = Array(RequestMethod.POST), headers = Array("content-type=application/json"), consumes = Array("application/json"))
    def idcards( @PathVariable("userid")  userId:String ,@Valid @RequestBody usercard : Card,result: BindingResult): Card = {
    		    if (result.hasErrors()) {
      throw new ParameterMissingException(result.toString)
    } 
    else 
    {  var map_u2 = map_usr(userId)
    	   map_u2.makecardmap(usercard)
    	  this.map_usr += (userId -> map_u2)
    	  return map_u2.card
    }
   }
   
   @RequestMapping(value=Array("/api/V1/users/{userid}/idcards"), method=Array(RequestMethod.GET), produces = Array("application/json"), headers=Array("content-type=application/json"))
    def viewcards(@PathVariable("userid")  userId:String ):java.util.Map[String,Card]={
    var map_u3 = map_usr(userId)
    return map_u3.map_card // cardmap
   }
   
   @RequestMapping(value=Array("/api/V1/users/{userid}/idcards/{card_id}"), method=Array(RequestMethod.DELETE), headers=Array("content-type=application/json"))@ResponseStatus(HttpStatus.NO_CONTENT)
	def deletecards(@PathVariable("userid")  userId:String, @PathVariable("card_id")  cardId:String ):Unit={
     var map_u4 = map_usr(userId)
     map_u4.map_card  -= cardId
     map_usr += (userId -> map_u4)
   }
   
   @RequestMapping(value = Array("/api/V1/users/{userid}/weblogins"), method = Array(RequestMethod.POST), headers = Array("content-type=application/json"), consumes = Array("application/json"))
    def webloginscards( @PathVariable("userid")  userId:String ,@Valid @RequestBody userlogin : Web,result: BindingResult): Web = {
    if (result.hasErrors()) {
      throw new ParameterMissingException(result.toString)
    } 
    else 
    {
     var map_u5 = map_usr(userId)
    		   map_u5.makewebmap(userlogin)
    		   this.map_usr += (userId -> map_u5)
    		   return map_u5.web
    }  
     }
   
   @RequestMapping(value=Array("/api/V1/users/{userid}/weblogins"), method=Array(RequestMethod.GET), produces = Array("application/json"), headers=Array("content-type=application/json"))
	def viewweb(@PathVariable("userid")  userId:String ):java.util.Map[String,Web]={
    var map_u6 = map_usr(userId)
    return map_u6.map_web 
   }
   
   @RequestMapping(value=Array("/api/V1/users/{userid}/weblogins/{login_id}"), method=Array(RequestMethod.DELETE), headers=Array("content-type=application/json"))@ResponseStatus(HttpStatus.NO_CONTENT)
	def deleteweb(@PathVariable("userid")  userId:String, @PathVariable("login_id")  loginId:String ):Unit={
     var map_u7 = map_usr(userId)
     map_u7.map_web  -= loginId
     map_usr += (userId -> map_u7)
   }
   
    @RequestMapping(value = Array("/api/V1/users/{userid}/bankaccounts"), method = Array(RequestMethod.POST), headers = Array("content-type=application/json"), consumes = Array("application/json"))
    def userbank( @PathVariable("userid")  userId:String ,@Valid @RequestBody bank : Bank,result: BindingResult): Bank = {
    if (result.hasErrors()) {
      throw new ParameterMissingException(result.toString)
    } 
    else 
    {
      var map_u8 = map_usr(userId)
    		   map_u8.makebankmap(bank)
    		   this.map_usr += (userId -> map_u8)
    		   return map_u8.bank
    }  
     }
    
   @RequestMapping(value=Array("/api/V1/users/{userid}/bankaccounts"), method=Array(RequestMethod.GET), produces = Array("application/json"), headers=Array("content-type=application/json"))
	def viewbank(@PathVariable("userid")  userId:String ):java.util.Map[String,Bank]={
    var map_u9 = map_usr(userId)
    return map_u9.map_bank
   }
   
   @RequestMapping(value=Array("/api/V1/users/{userid}/bankaccounts/{ba_id}"), method=Array(RequestMethod.DELETE), headers=Array("content-type=application/json"))@ResponseStatus(HttpStatus.NO_CONTENT)
	def deletebank(@PathVariable("userid")  userId:String, @PathVariable("ba_id")  baId:String ):Unit={
     var map_u10 = map_usr(userId)
     map_u10.map_bank -= baId
     map_usr += (userId -> map_u10)
   }
   
   /*@RequestMapping(value=Array("/api/V1/users/counter"), method=Array(RequestMethod.GET))
	@ResponseBody
   def counter( ): String={
    var countValue =""
	var key = "0"
	var result = ""
	var flag = false
		
		try{
		println("Sending: " + key)
		result = this.client_cnt.get(key)
		println("Received: " + result)
	}
	catch{
		
		case e : Exception => println("Exception: " + e)

		var keyval = this.client_cnt.set(key, "0")
		flag = true
		
	}
		
		if(!flag)
		{
			var result = this.client_cnt.get(key)
			var counter1 = result.toInt
			var counter2 = counter1 + 1
			var update = this.client_cnt.set(key, counter2.toString)
			countValue =  this.client_cnt.get(key)
 		}
		

	return countValue

   
   }*/


   @RequestMapping(value=Array("/api/V1/users/counter"), method=Array(RequestMethod.GET))
   @ResponseBody
   def counter( ): String={
		var key = "/009994127/counter"
		var result : EtcdResult = null
		try{
			result = this.client_cnt.get(key)
		}
		catch{
			case e : Exception => println("Exception: " + e)
			result = this.client_cnt.set(key, "0")		
		}
		result = this.client_cnt.get(key)
		var intCount = result.node.value.toInt
		var finalCount = intCount + 1
		var update = this.client_cnt.set(key, finalCount.toString)
		result = this.client_cnt.get(key)
		return result.node.value
  }
   
}
   