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
}getJsonAsync('/dummy.json')
  .then(data => console.log(data)); 
