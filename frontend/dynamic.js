var circles = [];
async function getJsonAsync(name)
{
	let response = await fetch(`${name}`);
	let data = await response.json()
	return data;
}
getJsonAsync('/haltestellen.json')
	.then(function(data) {
	for (j = 0; j < data.length; j++){
		var aktuelle_haltestelle = data[j]
        L.circle([aktuelle_haltestelle.longitude, aktuelle_haltestelle.latitude], {
           color: 'blue',
           fillColor: '#00c1ff',
           fillOpacity: 0.2,
           radius: 20
		}).addTo(mymap);
	}
	return data;
}).then(function(output) {
});
