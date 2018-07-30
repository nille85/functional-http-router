package be.nille.http.router.exception

import spock.lang.Specification
import spock.lang.Unroll

class ResultTest extends Specification {


    @Unroll
    def "success of one type should be converted to success of other type"() {
        given:
        def result = Result.ofSuccess(stringValue)

        when:
        Result<Boolean> mapped = result.map { it.equals("value") }

        then:
        isSuccess == mapped.isSuccess()
        booleanValue == mapped.value.get()

        where:
        stringValue | isSuccess | booleanValue
        "value"     | true      | true
        "notvalue"  | true      | false
        "3"         | true      | false


    }

    def "failure should remain a failure after using a map function"() {
        given:
        def result = Result.ofFailure(ErrorMessage.of("Error occurred", ErrorMessage.Severity.CRITICAL))

        when:
        Result<Boolean> mapped = result.map { it.equals("value") }

        then:
        mapped.isFailure()
        mapped.getErrorMessage().get().message == "Error occurred"
        mapped.getErrorMessage().get().severity == ErrorMessage.Severity.CRITICAL

    }
}
