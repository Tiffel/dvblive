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
		var tramline = data[i];
		for (j = 0; j < tramline.haltestellen.length; j++){
			var aktuelle_haltestelle = tramline.haltestellen[j]
            L.circle([aktuelle_haltestelle.longitude, aktuelle_haltestelle.latitude], {
                color: 'blue',
                fillColor: '#00c1ff',
                fillOpacity: 0.2,
                radius: 20
			}).addTo(mymap);
		}
	}
	return data;
}).then(function(output) {
});
