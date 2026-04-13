package es.upm.miw.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void testBadGatewayException() {
        BadGatewayException ex = new BadGatewayException("detail");
        assertThat(ex.getMessage()).isEqualTo("Bad Gateway Exception. detail");
    }

    @Test
    void testBadRequestException() {
        BadRequestException ex = new BadRequestException("detail");
        assertThat(ex.getMessage()).isEqualTo("Bad Request Exception. detail");
    }

    @Test
    void testConflictException() {
        ConflictException ex = new ConflictException("detail");
        assertThat(ex.getMessage()).isEqualTo("Conflict Exception. detail");
    }

    @Test
    void testForbiddenException() {
        ForbiddenException ex = new ForbiddenException("detail");
        assertThat(ex.getMessage()).isEqualTo("Forbidden Exception. detail");
    }

    @Test
    void testNotFoundException() {
        NotFoundException ex = new NotFoundException("detail");
        assertThat(ex.getMessage()).isEqualTo("Not Found Exception. detail");
    }

    @Test
    void testInternalServerException() {
        InternalServerException ex = new InternalServerException("detail");
        assertThat(ex.getMessage()).isEqualTo("Internal Server Exception. detail");
    }

    @Test
    void testErrorMessage() {
        NotFoundException ex = new NotFoundException("not found");
        ErrorMessage errorMessage = new ErrorMessage(ex, 404);
        assertThat(errorMessage.getError()).isEqualTo("NotFoundException");
        assertThat(errorMessage.getMessage()).isEqualTo("Not Found Exception. not found");
        assertThat(errorMessage.getCode()).isEqualTo(404);
    }
}