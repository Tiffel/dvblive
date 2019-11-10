var circles = [];
async function getJsonAsync(name) 
{
	let response = await fetch(`${name}`);
	let data = await response.json()
	return data;
}
getJsonAsync('/dummy12.json')
	.then(function(data) {
	for (i = 0; i < data.length; i++){
		circles[i] = []
		var tramline = data[i];
		for (j = 0; j < tramline.haltestellen.length; j++){
			var aktuelle_haltestelle = tramline.haltestellen[j]
			circles[i][j] = L.circle([aktuelle_haltestelle.latitude, aktuelle_haltestelle.longitude], {
				color: 'red',
				fillColor: '#f03',
				fillOpacity: 0.5,
				radius: 500
			}).addTo(mymap);
		}
	}
	return data;
}).then(function(output) {
	console.log('Berechnung fertig')
	console.log(circles)
}); 
