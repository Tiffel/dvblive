var latlngs = [
	[51.04, 13.74],
	[51.05, 13.75],
	[51.03, 13.7]
];
var polyline = L.polyline(latlngs, {color: 'red'}).addTo(mymap);
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
}).then(output => console.log(output)); 
