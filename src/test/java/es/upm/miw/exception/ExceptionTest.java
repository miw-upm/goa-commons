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
    void testUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("detail");
        assertThat(ex.getMessage()).isEqualTo("Unauthorized Exception. detail");
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
        ErrorMessage errorMessage = new ErrorMessage(ex);
        assertThat(errorMessage.getError()).isEqualTo("NotFoundException");
        assertThat(errorMessage.getMessage()).isEqualTo("not found");
        assertThat(errorMessage.getCause()).isEqualTo("");
    }

    @Test
    void testErrorMessageWithCauseChain() {
        IllegalArgumentException source = new IllegalArgumentException("invalid data");
        BadRequestException badRequestException = new BadRequestException("bad payload", source);
        InternalServerException ex = new InternalServerException("cannot process request", badRequestException);
        ErrorMessage errorMessage = new ErrorMessage(ex);

        assertThat(errorMessage.getError()).isEqualTo("InternalServerException");
        assertThat(errorMessage.getMessage()).isEqualTo("cannot process request");
        assertThat(errorMessage.getCause()).isEqualTo(
                "BadRequestException: Bad Request Exception. bad payload -> IllegalArgumentException: invalid data");
    }
}
