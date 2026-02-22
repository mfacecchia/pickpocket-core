package com.feis.splitnings.features.issuedPaycheck.data.dto.request;

import com.feis.splitnings.common.data.dto.request.BaseUpdateDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// NOTE: Issued paychecks are not updatable. Keep this implementation as-is
// unless there's a valid reason to modify it
@Getter
@Setter
@NoArgsConstructor
public class IssuedPaycheckUpdateDto extends BaseUpdateDto {
}
