const urlParams = new URLSearchParams(window.location.search);
const userId = urlParams.get('id');
const groupId = urlParams.get('groupId');

console.log(userId)
console.log(groupId)