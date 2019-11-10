var abschnitte = [];
async function getJsonAsync(name)
{
	let response = await fetch(`${name}`);
	let data = await response.json()
	return data;
}
getJsonAsync('/haltestellen.json')
	.then(function(data) {
	for (i = 0; i < data.length; i++){
		var aktuelle_haltestelle = data[i]
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
		var tooltip = "";
		for (tooltip_count = 0; tooltip_count < verbindung.linien.length; tooltip_count++){
			tooltip += "Linie " + verbindung.linien[tooltip_count] + "<br>";
		}
		var farbe = "green";
		if (verbindung.maxVerspaetung >= 60){
			farbe = "orange";
		}
		if (verbindung.maxVerspaetung >= 180){
			farbe = "red";
		}
		L.polyline(latlngs, {color: farbe}).addTo(mymap).bindTooltip(tooltip);
	}
});
