package com.training.trackMailApi.service.impl;

import com.training.trackMailApi.converter.PostOfficeConverter;
import com.training.trackMailApi.dao.PostOfficeDAO;
import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.model.PostOffice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostOfficeServiceImplTest {

    @Mock
    private PostOfficeDAO postOfficeDAO;

    @Mock
    private PostOfficeConverter postOfficeConverter;

    @InjectMocks
    private PostOfficeServiceImpl postOfficeService;

    @Test
    public void saveTest() {
        PostOfficeDto postOfficeDto = new PostOfficeDto();

        postOfficeDto.setIndex("212008");
        postOfficeDto.setName("Mogilev Central Post Office");

        PostOffice postOffice = new PostOffice();

        postOffice.setIndex(postOfficeDto.getIndex());
        postOffice.setName(postOffice.getName());

        when(postOfficeConverter.fromPostOfficeDto(postOfficeDto)).thenReturn(postOffice);

        PostOffice result = postOfficeService.save(postOfficeDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(postOffice.getIndex(), result.getIndex());

        verify(postOfficeDAO, times(1)).save(postOfficeConverter.fromPostOfficeDto(postOfficeDto));
    }

    @Test
    public void getAllTest() {
        PostOffice postOfficeOne = createTestPostOffice(1L, "Mogilev Central Post Office", "212008");
        PostOffice postOfficeTwo = createTestPostOffice(2L, "Minsk Central Post Office", "222060");
        PostOffice postOfficeThree = createTestPostOffice(3L, "Singapore Central Post Office", "750406");

        List<PostOffice> postOffices = asList(postOfficeOne, postOfficeTwo, postOfficeThree);

        List<PostOfficeDto> expected = postOffices
                .stream()
                .map(postOfficeConverter::convertToPostOfficeDto)
                .collect(Collectors.toList());

        when(postOfficeDAO.getAll()).thenReturn(postOffices);
        when(postOfficeConverter.convertToListPostOfficeDto(postOffices)).thenReturn(expected);

        List<PostOfficeDto> actual = postOfficeService.getAll();

        Assert.assertEquals(3, actual.size());
        Assert.assertEquals(3, expected.size());
        Assert.assertEquals(expected, actual);

        verify(postOfficeDAO, times(1)).getAll();
        verify(postOfficeConverter, times(1)).convertToListPostOfficeDto(postOffices);
    }

    @Test
    public void getByIdTest() {
        PostOffice postOffice = createTestPostOffice(1L, "Mogilev Central Post Office", "212008");

        when(postOfficeDAO.getById(postOffice.getId())).thenReturn(postOffice);

        PostOfficeDto expected = postOfficeConverter.convertToPostOfficeDto(postOffice);

        PostOfficeDto actual = postOfficeService.getById(postOffice.getId());

        Assert.assertEquals(expected, actual);

        verify(postOfficeDAO, times(1)).getById(postOffice.getId());
    }

    private PostOffice createTestPostOffice(Long id, String name, String index) {
        PostOffice postOffice = new PostOffice();

        postOffice.setId(id);
        postOffice.setName(name);
        postOffice.setIndex(index);

        return postOffice;
    }
}
