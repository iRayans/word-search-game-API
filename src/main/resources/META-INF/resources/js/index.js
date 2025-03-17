import { Grid } from "./grid.js";

const submitWordBtn = document.querySelector(".submit-word");

submitWordBtn.addEventListener("click", async () => {
  const grid = new Grid();
  const commasSeparatedWords = document.querySelector("#add-word").value;
  const gridSize = document.querySelector("#grid-size").value;
  let result = await fetchGridInfo(gridSize,commasSeparatedWords);
  grid.words = commasSeparatedWords.split(",");
  grid.renderGrid(gridSize, result);
  let wordListNode = document.createTextNode(grid.words);
  let wordListSection = document.querySelector(".word-list");
  if (wordListSection.lastChild) {
    wordListSection.removeChild(wordListSection.lastChild);
}

wordListSection.appendChild(wordListNode)
});

async function fetchGridInfo(gridSize,commasSeparatedWords) {
  console.log("Params: " + commasSeparatedWords);
  let response = await fetch(
    `./wordgrid?gridSize=${gridSize}&words=${commasSeparatedWords}`
  );
  let result = await response.text();
  return result.split(" ");
}
