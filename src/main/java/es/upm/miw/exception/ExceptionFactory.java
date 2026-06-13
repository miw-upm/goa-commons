package es.upm.miw.exception;

public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    public static RuntimeException from(String error, String detail) {
        return switch (error) {
            case "BadGatewayException"        -> new BadGatewayException(detail);
            case "BadRequestException"        -> new BadRequestException(detail);
            case "ConflictException"          -> new ConflictException(detail);
            case "ForbiddenException"         -> new ForbiddenException(detail);
            case "InfrastructureException"    -> new InfrastructureException(detail);
            case "InternalServerException"    -> new InternalServerException(detail);
            case "InvalidTransitionException" -> new InvalidTransitionException(detail);
            case "NotFoundException"          -> new NotFoundException(detail);
            case "UnauthorizedException"      -> new UnauthorizedException(detail);

            default -> new InternalServerException(
                    "Error no reconocido [" + error + "]. " + detail);
        };
    }
}
