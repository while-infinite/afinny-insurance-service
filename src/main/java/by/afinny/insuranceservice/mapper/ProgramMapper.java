package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ProgramMapper {

    List<ProgramDto> toProgramDtoList(List<Program> programs);

    @Mapping(source = "id", target = "programId")
    ProgramDto toProgramDtoList(Program program);
}
