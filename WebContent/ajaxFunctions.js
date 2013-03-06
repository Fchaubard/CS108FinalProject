var request; function getName(){
var roll = document.getElementById("roll").value;
var url = "http://localhost:8080/blog_demo/StudentInfo?roll="+roll;
if(window.ActiveXObject)
{ 
	request = new ActiveXObject("Microsoft.XMLHTTP"); 
}
else if(window.XMLHttpRequest){ request = new XMLHttpRequest(); } request.onreadystatechange = showResult;
	request.open("POST",url,true);
	request.send();
}
function showResult(){
	if(request.readyState == 4){
		var response = request.responseXML;
		var students = response.getElementsByTagName("Student");
		var student = students[0];
		document.getElementById("NamelH1").innerHTML = student.getElementsByTagName("Name")[0].text;
		document.getElementById("HostelH1").innerHTML = student.getElementsByTagName("Hostel")[0].text;
		document.getElementById("ContactH1").innerHTML = student.getElementsByTagName("Contact")[0].text;
	}
}
