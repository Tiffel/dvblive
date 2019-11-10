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
});
getJsonAsync('/abschnitte.json')
	.then(function(data) {
	for (i = 0; i < data.length; i++){
		var verbindung = data[i];
		var latlngs = [
		    [verbindung.startPosition.longitude, verbindung.startPosition.latitude],
		    [verbindung.endPosition.longitude, verbindung.endPosition.latitude]
		];
		var farbe = "green";
		if (verbindung.maxVerspaetung > 60){
			farbe = "orange";
		}
		if (verbindung.maxVerspaetung > 300){
			farbe = "red";
		}
		L.polyline(latlngs, {color: farbe}).addTo(mymap);
	}
});
