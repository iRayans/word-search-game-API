package com.rayan.wordsearch_api.resource;

import com.rayan.wordsearch_api.service.WordGridService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;

@Path("/wordgrid")
public class WordSearchResource {

    @Inject
    WordGridService wordGridService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String createWordGrid(@QueryParam("gridSize") int gridSize, @QueryParam("words") String wordList) {
        System.out.println("params: " + wordList + " gridSize: " + gridSize);
        // Split using both English `,` and Arabic `،`
        List<String> words = Arrays.asList(wordList.split("[,،]"));
        System.out.println("words: " + words);
        char[][] grid = wordGridService.generateGrid(gridSize, words);
        String gridToString = "";
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridToString += grid[i][j] + " ";
            }
            gridToString += "\r\n";
        }
        return gridToString;
    }
}

