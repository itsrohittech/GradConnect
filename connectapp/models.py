from django.contrib.auth.models import User
from django.db import models




class Admin_user(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    msg = models.CharField(max_length=200, null=True, blank=True)



class MyCustomUser(User):
    # Additional fields
    dob = models.DateField(null=True, blank=True)  # Date of birth
    age = models.IntegerField(null=True, blank=True)
    gender = models.CharField(max_length=10, null=True, blank=True)
    course = models.CharField(max_length=100, null=True, blank=True)
    department = models.CharField(max_length=100, null=True, blank=True)
    user_type = models.CharField(max_length=20, null=True, blank=True)
    status = models.CharField(max_length=20, default='Pending')

    def __str__(self):
        return self.username



class StartupStatus(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    startup_name = models.CharField(max_length=255)
    description = models.TextField()
    year_started = models.IntegerField()
    is_active = models.BooleanField(default=True)

    def __str__(self):
        return self.startup_name



class MaritalStatus(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    status = models.CharField(max_length=20, blank=True, null=True)
    spouse_name = models.CharField(max_length=255, blank=True, null=True)
    marriage_date = models.DateField(blank=True, null=True)

    def __str__(self):
        return f'{self.user.username} - {self.status}'
    



class HigherStudiesStatus(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    degree = models.CharField(max_length=255)
    institution_name = models.CharField(max_length=255)
    start_year = models.IntegerField()
    end_year = models.IntegerField(blank=True, null=True)  # If still studying, end_year can be null
    is_completed = models.BooleanField(default=False)

    def __str__(self):
        return self.degree




class BusinessStatus(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    business_name = models.CharField(max_length=255)
    business_type = models.CharField(max_length=100)
    year_started = models.IntegerField()
    description = models.TextField()
    is_active = models.BooleanField(default=True)

    def __str__(self):
        return self.business_name
    



class OtherStatus(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    description = models.TextField()
    is_active = models.BooleanField(default=True)

    def __str__(self):
        return f'{self.user.username} - Other Status'