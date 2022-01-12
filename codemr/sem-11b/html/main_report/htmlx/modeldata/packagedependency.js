var matrix = [[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[3,0,0,0,0,0,5,8,3,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0],[2,0,0,0,3,0,2,4,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0],[0,0,0,0,2,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0],[2,0,0,0,0,0,2,4,2,0,0,0,0,0,0,0,0,0,0,4,0,0,4,1,0,0],[1,0,0,0,0,0,3,3,3,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0],[2,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[5,2,0,0,0,0,6,6,3,0,0,4,4,0,0,0,0,2,0,0,0,0,0,1,0,1],[0,0,1,0,2,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0],[3,0,0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[2,0,0,0,0,0,4,5,2,0,0,0,0,0,0,1,0,0,0,0,2,1,0,1,1,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[2,3,0,0,0,0,4,4,1,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[1,0,0,0,0,0,1,1,3,0,0,5,2,0,0,0,0,0,0,0,0,0,0,1,0,0]]
var packages = [{
"name": " nl.tudelft.sem11b.data", "color": " #3182bd"
}
,{
"name": " nl.tudelft.sem11b.clients", "color": " #6baed6"
}
,{
"name": " nl.tudelft.sem11b.authentication.controllers", "color": " #9ecae1"
}
,{
"name": " nl.tudelft.sem11b.authentication", "color": " #c6dbef"
}
,{
"name": " nl.tudelft.sem11b.authentication.services", "color": " #e6550d"
}
,{
"name": " nl.tudelft.sem11b.admin.data.controllers", "color": " #fd8d3c"
}
,{
"name": " nl.tudelft.sem11b.services", "color": " #fdae6b"
}
,{
"name": " nl.tudelft.sem11b.data.models", "color": " #fdd0a2"
}
,{
"name": " nl.tudelft.sem11b.data.exceptions", "color": " #31a354"
}
,{
"name": " nl.tudelft.sem11b.admin.services", "color": " #74c476"
}
,{
"name": " nl.tudelft.sem11b.authentication.filters", "color": " #a1d99b"
}
,{
"name": " nl.tudelft.sem11b.admin.data.entities", "color": " #c7e9c0"
}
,{
"name": " nl.tudelft.sem11b.admin.data.repositories", "color": " #756bb1"
}
,{
"name": " nl.tudelft.sem11b.admin", "color": " #9e9ac8"
}
,{
"name": " nl.tudelft.sem11b.data.sqlite", "color": " #bcbddc"
}
,{
"name": " nl.tudelft.sem11b.reservation.entity", "color": " #dadaeb"
}
,{
"name": " nl.tudelft.sem11b.http", "color": " #636363"
}
,{
"name": " nl.tudelft.sem11b.admin.data", "color": " #969696"
}
,{
"name": " nl.tudelft.sem11b.reservation", "color": " #bdbdbd"
}
,{
"name": " nl.tudelft.sem11b.authentication.repositories", "color": " #d9d9d9"
}
,{
"name": " nl.tudelft.sem11b.reservation.services", "color": " #3182bd"
}
,{
"name": " nl.tudelft.sem11b.reservation.repository", "color": " #6baed6"
}
,{
"name": " nl.tudelft.sem11b.authentication.entities", "color": " #9ecae1"
}
,{
"name": " nl.tudelft.sem11b.data.exception", "color": " #c6dbef"
}
,{
"name": " nl.tudelft.sem11b", "color": " #e6550d"
}
,{
"name": " nl.tudelft.sem11b.admin.data.filters", "color": " #fd8d3c"
}
];
