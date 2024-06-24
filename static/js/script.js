/**
 
function openNav() {
  document.getElementById("mySidebar").style.width = "250px";
  document.getElementById("main").style.marginLeft = "250px";
}

function closeNav() {
  document.getElementById("mySidebar").style.width = "0";
  document.getElementById("main").style.marginLeft= "0";
}
*/

//search

const search=()=>{

  let query=$("#search-input").val();

  if(query==""){
    $(".search-result").hide();

  }
  else{
    
  let url=`http://localhost:8080/search/${query}`;

  fetch(url)
  .then((response) =>{
    return response.json();
  })
  .then((data) =>{
   
    let text=`<div class='list-group'>`;
    data.forEach(contact => {
      text+=`<a href='/user/contacts/${contact.cid}' class='list-group-item list-group-action'> ${contact.name}</a>`
    });
   
    text+=`</div>`;
    $(".search-result").html(text);
    
    $(".search-result").show();
  });

  }

};




