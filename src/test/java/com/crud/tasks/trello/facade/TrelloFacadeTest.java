package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TrelloFacadeTest {

    @Test
    public void mapperTests() {
        //Given
        TrelloMapper trelloMapper = new TrelloMapper();
        TrelloList trelloList = new TrelloList("100", "tetList", true);
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(trelloList);
        TrelloBoard trelloBoard = new TrelloBoard("111", "tetBoard", trelloLists);
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(trelloBoard);
        TrelloCard trelloCard = new TrelloCard("testCard", "testing", "test", "100");
        //When
        List<TrelloBoardDto> trelloBoardsDto = trelloMapper.mapToBoardsDto(trelloBoards);
        TrelloCardDto trelloCardDto = trelloMapper.mapToCardDto(trelloCard);
        List<TrelloBoard> trelloBoardsMapped = trelloMapper.mapToBoards(trelloBoardsDto);
        TrelloCard trelloCardMapped = trelloMapper.mapToCard(trelloCardDto);
        //Than
        assertEquals("tetBoard", trelloBoardsDto.get(0).getName());
        assertEquals("100", trelloBoardsDto.get(0).getLists().get(0).getId());
        assertEquals("testing", trelloCardDto.getDescription());
        assertEquals("111", trelloBoardsMapped.get(0).getId());
        assertEquals(true, trelloBoardsMapped.get(0).getLists().get(0).isClosed());
        assertEquals("testCard", trelloCardMapped.getName());
    }
}
