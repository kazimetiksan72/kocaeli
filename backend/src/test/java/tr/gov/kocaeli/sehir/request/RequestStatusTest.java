package tr.gov.kocaeli.sehir.request;
import org.junit.jupiter.api.Test;import tr.gov.kocaeli.sehir.request.domain.RequestStatus;import static org.assertj.core.api.Assertions.*;
class RequestStatusTest{
 @Test void acceptsOperationalHappyPath(){assertThat(RequestStatus.NEW.canTransitionTo(RequestStatus.PRE_REVIEW)).isTrue();assertThat(RequestStatus.PRE_REVIEW.canTransitionTo(RequestStatus.ASSIGNED)).isTrue();assertThat(RequestStatus.ASSIGNED.canTransitionTo(RequestStatus.FIELD_REVIEW)).isTrue();assertThat(RequestStatus.FIELD_REVIEW.canTransitionTo(RequestStatus.DECISION_PENDING)).isTrue();assertThat(RequestStatus.DECISION_PENDING.canTransitionTo(RequestStatus.CONVERTED_TO_PROJECT)).isTrue();}
 @Test void rejectsSkippingApproval(){assertThat(RequestStatus.NEW.canTransitionTo(RequestStatus.CONVERTED_TO_PROJECT)).isFalse();assertThat(RequestStatus.ASSIGNED.canTransitionTo(RequestStatus.CONVERTED_TO_ACTIVITY)).isFalse();}
}

