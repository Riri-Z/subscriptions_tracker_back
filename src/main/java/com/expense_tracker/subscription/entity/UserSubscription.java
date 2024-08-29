package com.expense_tracker.subscription.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.expense_tracker.common.entity.BaseEntity;
import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;
import com.expense_tracker.user.entity.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = { "userInfo", "subscription" })
@Table(name = "user_subscription",
		uniqueConstraints = { @UniqueConstraint(columnNames = { "user_info_id", "subscription_id" }) })
public class UserSubscription extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "subscription_start_date", columnDefinition = "TIMESTAMP")
	private ZonedDateTime startDate;

	@NotNull
	@Future
	@Column(name = "subscription_end_date", columnDefinition = "TIMESTAMP")
	private ZonedDateTime endDate;

	@NotNull
	@Column(name = "renewal_date", columnDefinition = "TIMESTAMP")
	private ZonedDateTime renewalDate;

	@NotNull
	@Column(name = "amount")
	private BigDecimal amount;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "billing_cycle")
	private BillingCycle billingCycle; // 'monthly', 'yearly' etc.

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private SubscriptionStatus status; // 'active', 'cancelled', 'paused'

	@ManyToOne
	@JoinColumn(name = "user_info_id")
	private UserInfo userInfo;

	@ManyToOne
	@JoinColumn(name = "subscription_id")
	private Subscription subscription;

}
