$(document).ready(function()
{
	var infobox = $("#infobox");
	infobox.find(".infobox").each(function(idx)  // keep only first infobox, remove the rest
	{
		if(idx != 0) $(this).remove();
	});
	
	infobox.find("table, tr, th, td, div").removeAttr("style"); 
	infobox.find("div br").remove();
	infobox.find("#Timeline-row").remove();
	if(infobox.find(".fn, .org").size() == 0)
	{
		infobox.find(".infobox tr:first th").addClass("fn"); 
	}
	
	infobox.find(".image").each(function(idx) // keep only first image
	{
		if(idx != 0) $(this).closest("tr").remove();
	});
	
	infobox.find("a").each(function(idx) // use absolute URL for <a>
	{
		var url = $(this).attr("href");
		if(url.substring(0,6) == "/wiki/")
			$(this).attr("href", "http://en.wikipedia.org" + url);
	});	
});