package com.rbs.training.supplychain.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rbs.training.supplychain.model.ChartOfAccount;
import com.rbs.training.supplychain.model.GeneralLedger;
import com.rbs.training.supplychain.service.AccountingManagementService;

@Component
@RestController
@RequestMapping("/ACM")
public class AccountingManagementController {
	AccountingManagementService accountingManagementServiceObj = new AccountingManagementService();
	//@Autowired

	 @RequestMapping("/test")
	    public String service1() {
	        return "Hello, World!" ;
	    }
	 @RequestMapping(value = "/viewGL",method = RequestMethod.GET)
	 public String ViewLedger(){
	
			String resultString="<form action='http://localhost:8181/ACM/viewGLBySearch'>"+"enter the account entry number"+"<br>"+
	    	 "<input type='text' name='search'></input>"+"<input type='submit' value='search'>"+"</form>";
			List<GeneralLedger> generalledgerlists=accountingManagementServiceObj.getEachGLEntry();
			resultString +="<table>";
			resultString +="<tr><th>Account Entry No.</th><th>Current Date</th><th>Payment Date</th><th>Transaction No</th><th>Customer Account No.</th><th>Invoice No.</th><th>Dr/Cr</th><th>Amount</th><th>Due Date</th></tr>";
			for(GeneralLedger generalledgerlist:generalledgerlists){
								resultString += "<tr><td>" + generalledgerlist.getAccountEntryNo() + "</td><td>"+
								generalledgerlist.getCurrentDate()  + "</td><td>"+
								generalledgerlist.getPaymentDate()  + "</td><td>"+
								generalledgerlist.getTransactionNo() +"</td><td>"+
								generalledgerlist.getCustomerAccountNo()+"</td><td>"+
								generalledgerlist.getInvoiceNo()+"</td><td>"+
								generalledgerlist.getDrOrCr()+"</td><td>"+
								generalledgerlist.getAmount() +"</td><td>"+
								generalledgerlist.getDueDate() + "</td></tr>";
	       }
			resultString +="</table>";
			return resultString;
		}
	 @RequestMapping(value = "/viewGLBySearch",method = RequestMethod.GET)
	 @ResponseBody
	 public String ViewLedgerBySearch(HttpServletRequest request,HttpServletResponse response){
	
			String resultString="";
			String searchBy=request.getParameter("search");
			List<GeneralLedger> generalledgerlists=accountingManagementServiceObj.getEachGLEntryBySearch(searchBy);
			resultString +="<table>";
			int flag=0;
			for(GeneralLedger generalledgerlist:generalledgerlists){
				if(flag==0)
				{
					resultString +="<tr><th>Account Entry No.</th><th>Current Date</th><th>Payment Date</th><th>Transaction No</th><th>Customer Account No.</th><th>Invoice No.</th><th>Dr/Cr</th><th>Amount</th><th>Due Date</th></tr>";
				}
				flag=1;
				resultString += "<tr><td>" + generalledgerlist.getAccountEntryNo() + "</td><td>"+
						generalledgerlist.getCurrentDate()  + "</td><td>"+
						generalledgerlist.getPaymentDate()  + "</td><td>"+
						generalledgerlist.getTransactionNo() +"</td><td>"+
						generalledgerlist.getCustomerAccountNo()+"</td><td>"+
						generalledgerlist.getInvoiceNo()+"</td><td>"+
						generalledgerlist.getDrOrCr()+"</td><td>"+
						generalledgerlist.getAmount() +"</td><td>"+
						generalledgerlist.getDueDate() + "</td></tr>";
	       }
			resultString +="</table>";
			if(flag==0)
			{
				try{
				response.sendRedirect("http://localhost:8181/ACM/viewGL");
				}
				catch(Exception e)
				{
			    	System.out.println("Exception " + e.getMessage());
			    }
			}
			
			resultString+="<br><form action='http://localhost:8181/ACM/viewGL'>"+
	    	"<input type='submit' value='back'>"+"</form>";
			return resultString;
		}
	 /*@RequestMapping(value = "/viewCOA/{productSwiftID}",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	 public ChartOfAccount getCOAinJSON(@PathParam("productSwiftID") String ProductSwiftID){
			
			AccountingManagementService acmDAO=new AccountingManagementService();
			ChartOfAccount coa=acmDAO.getChartOfAccountValues(ProductSwiftID);
			
			return coa;
		}*/
	
	 @RequestMapping(value = "/viewCOAlist",method = RequestMethod.GET)
	 public String dispCOAlist()
	 {
		 	List<ChartOfAccount> coaList=accountingManagementServiceObj.getCOAList();
			String resultString="<html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'><title>Chart of Accounts List</title><script type='text/javascript' src='chartOfAccountsList.js'></script></head><body><h1>Chart of Accounts(Swift ID)</h1><br>";
			
			resultString +="<div id='chartList'><form action='http://localhost:8181/ACM/delCOA' method='post'>";
			int flag=0;
			resultString +="<table>";
			for(ChartOfAccount coa:coaList)
			{
				if(flag==0)
					resultString+="<tr><th></th><th>Head</th><th>Legal Entity</th><th>Country</th><th>Branch</th><th>Product</th><th>Currency</th><th>Book</th><th>SWIFT ID</th></tr>";
				flag=1;
				//resultString +="<a href='http://localhost:8181/index.html'>"+s+"</a><br>\n";
				//String url="ChartOfAccountsEdit.jsp?chSID="+s;
				String url="http://localhost:8181/index.html";
				resultString+="<tr><td><input type='checkbox' name='chartGroup' value='"+coa.getProductSwiftID()+"'/></td><td>"+coa.getHead()+"</td><td>"+coa.getLegalEntity()+"</td><td>"+coa.getCountry()+"</td><td>"+coa.getBranch()+"</td><td>"+coa.getProduct()+"</td><td>"+coa.getCurrency()+"</td><td>"+coa.getBook()+"</td><td>"+coa.getProductSwiftID()+"</td></tr>";
			}
			resultString +="</table>";
			if(flag==1)
				resultString +="<input type='submit' value='Delete Charts'><br>";
			resultString+="</form></div><div id='newEntrySpace'></div><a href='http://localhost:8181/addCOA.html'>Add a chart of Accounts</a><br><a href='http://localhost:8181/index.html'>Home</a></body></html>";
			return resultString;
	 }
	 
	 @RequestMapping(value = "/delCOA",method = RequestMethod.POST)
	 @ResponseBody
	 public void delCOA(HttpServletRequest request,HttpServletResponse response)
	 {
		 String[] chartNamesToDelete=request.getParameterValues("chartGroup");
		 List<String> res=new LinkedList<String>();
		 for(String chName:chartNamesToDelete)
			 res.add(chName);
		 try
		    {
			 	accountingManagementServiceObj.deleteCOA(res);
		    }

		    catch(Exception e)
		    {
		    	System.out.println("Exception " + e.getMessage());
		    }
		    finally
		    {
		    	try{
		    	response.sendRedirect("http://localhost:8181/ACM/viewCOAlist");
		    	}catch(Exception e)
			    {
			    	System.out.println("Exception " + e.getMessage());
			    }
		    }
	 }
	 @RequestMapping(value = "/addCOAContoller",method = RequestMethod.POST)
	 @ResponseBody
	 public void addCOAController(@PathParam("head") String head,@PathParam("legalEntity") String legalEntity,@PathParam("country") String country,@PathParam("branch") String branch,@PathParam("product") String product,@PathParam("currency") String currency,@PathParam("book") int book,@PathParam("productSwiftID") String productSwiftID,HttpServletResponse response)
	 {
		 System.out.println("inside addCOAController");
		 ChartOfAccount coa=new ChartOfAccount();
		 coa.setHead(head);
		 coa.setLegalEntity(legalEntity);
		 coa.setCountry(country);
		 coa.setBranch(branch);
		 coa.setProduct(product);
		 coa.setCurrency(currency);
		 coa.setBook(book);
		 coa.setProductSwiftID(productSwiftID);
		 System.out.println(coa.toString());
		 System.out.println("Before calling add");
		 accountingManagementServiceObj.addCOAService(coa);
		 System.out.println("After calling add");
	    try
	    {
	    	System.out.println("Inside redirect");
	    	response.sendRedirect("http://localhost:8181/ACM/viewCOAlist");
	    	
	    }
	    
	    catch(Exception e)
	    {
	    	System.out.println("Exception " + e.getMessage());
	    }
	 }
	 @RequestMapping(value = "/CheckCompliance",method = RequestMethod.GET)
	 @ResponseBody
	 public String complianceCheck(HttpServletRequest request,HttpServletResponse response){
	
		 
			String resultString="";
			String individualCountry=request.getParameter("Countryname");
			String individualname=request.getParameter("individualname");
			
			List<String> lstCountries = new ArrayList<String>();
			List<String> lstNames = new ArrayList<String>();
			
			lstCountries = accountingManagementServiceObj.sanctionedCountries();
			lstNames =accountingManagementServiceObj.sanctionedIndividuals();
			
		int c1=	checkCountry(lstCountries, individualCountry);
		int c2=	checkName(lstNames, individualname);
			
		if(c1==0||c2==0)
		{
			resultString="<b>Approval Denied. <br>Under Sanctioned List</b>";
		}
		else
			resultString="<b>Approval Sanctioned.</b>";
			
			return resultString;
		}
	 public int checkCountry(List<String> lstCountries,String country){
			
			int flag=0;
				for(int i=0;i<lstCountries.size();i++){
				    if(country.equalsIgnoreCase((String) lstCountries.get(i)))
				     {
				    	flag=1;
			     	}
			 	
				}
			  	if(flag==1)
					return 0;
			    else
				    return 1;
			}
			
	public int checkName(List<String> lstNames,String name){
				
				int flag=0;
					for(int i=0;i<lstNames.size();i++){
						if(name.equalsIgnoreCase((String) lstNames.get(i)))
					     {
					    	flag=1;
				     	}
				 	
					}
				  	if(flag==1)
						return 0;
				    else
					    return 1;
				}
}
