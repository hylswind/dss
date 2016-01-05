package eu.europa.esig.dss.EN319102.bbb.sav.checks;

import java.util.List;

import eu.europa.esig.dss.EN319102.bbb.ChainItem;
import eu.europa.esig.dss.jaxb.detailedreport.XmlSAV;
import eu.europa.esig.dss.validation.SignatureWrapper;
import eu.europa.esig.dss.validation.policy.rules.AttributeValue;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.MessageTag;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.report.DiagnosticData;
import eu.europa.esig.jaxb.policy.LevelConstraint;

public class CounterSignatureCheck extends ChainItem<XmlSAV> {

	private final DiagnosticData diagnosticData;
	private final SignatureWrapper signature;

	public CounterSignatureCheck(XmlSAV result, DiagnosticData diagnosticData, SignatureWrapper signature, LevelConstraint constraint) {
		super(result, constraint);
		this.diagnosticData = diagnosticData;
		this.signature = signature;
	}

	@Override
	protected boolean process() {
		boolean foundCountersignature = false;
		String currentSignatureId = signature.getId();

		List<SignatureWrapper> signatures = diagnosticData.getSignatures();
		for (SignatureWrapper signatureWrapper : signatures) {
			if (AttributeValue.COUNTERSIGNATURE.equals(signatureWrapper.getType()) && currentSignatureId.equals(signatureWrapper.getParentId())) {
				foundCountersignature = true;
				break;
			}
		}

		return foundCountersignature;
	}

	@Override
	protected MessageTag getMessageTag() {
		return MessageTag.BBB_SAV_IUQPCSP;
	}

	@Override
	protected MessageTag getErrorMessageTag() {
		return MessageTag.BBB_SAV_IUQPCSP_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return Indication.INVALID;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return SubIndication.SIG_CONSTRAINTS_FAILURE;
	}

}
