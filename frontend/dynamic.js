var polyline = [];
async function getJsonAsync(name) 
{
	let response = await fetch(`${name}`);
	let data = await response.json()
	return data;
}
getJsonAsync('/dummy.json')
	.then(function(data) {
	var output = [];
	var tramlines = data.tramlines;
	for (i = 0; i < tramlines.length; i++){
		var tramline = tramlines[i];
		var tramoutput = tramline;
		for (j = 0; j < tramline.points.length; j++){
			tramoutput.points[j] = [tramline.points[j].lat, tramline.points[j].long];
		}
		output.push(tramoutput);
	}
	return output;
}).then(function(output) {
	for (i = 0; i < output.length; i++){
		polyline[i] = L.polyline(output[i].points, {color: 'red'}).addTo(mymap);
	}
}); 
