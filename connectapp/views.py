from django.shortcuts import render,redirect
from django.contrib import messages
from django.contrib.auth import authenticate as auth_authenticate 
from django.contrib.auth import  login as auth_login 
from django.contrib.auth.decorators import login_required
from django.contrib.auth import logout as log
from django.http import JsonResponse
from datetime import date,timedelta
from django.shortcuts import render, get_object_or_404
from datetime import datetime
from django.db.models import Q
from django.contrib.auth.models import User
from .models import  MyCustomUser
from .models import StartupStatus
from .models import MaritalStatus
from .models import BusinessStatus
from .models import HigherStudiesStatus
from .models import OtherStatus,Admin_user




def user_msg_admin(request):
    # Fetching the messages for the logged-in user
    data = Admin_user.objects.filter(user=request.user)
    
    if request.method == 'POST':
        message = request.POST.get('message')  # Get the message from the form input
        
        # Fetching the logged-in user details
        user_det = MyCustomUser.objects.get(username=request.user)
        
        # Creating the message in the Admin_user model
        Admin_user.objects.create(user=user_det, msg=message)
    
    # Rendering the template with the data
    return render(request, 'user_msg_admin.html', {'data': data})




def admin_view_startup(request,id):
    try:
        user_det = MyCustomUser.objects.get(id = id)
        data = StartupStatus.objects.get(user = user_det)
    except:
        data = None 
        messages.success(request,'no data found')
        return redirect('/admin_view_user_detials/'+id+'/')
    return render(request,'admin_view_startup.html',{'data':data})

def admin_view_marital_status(request,id):
    try:
        user_det = MyCustomUser.objects.get(id = id)
        data = MaritalStatus.objects.get(user = user_det)
    except:
        data = None 
        messages.success(request,'no data found')
        return redirect('/admin_view_user_detials/'+id+'/')
    return render(request,'admin_view_marital_status.html',{'data':data})

def admin_view_business(request,id):
    try:
        user_det = MyCustomUser.objects.get(id = id)
        data = BusinessStatus.objects.get(user = user_det)
    except:
        data = None 
        messages.success(request,'no data found')
        return redirect('/admin_view_user_detials/'+id+'/')
    return render(request,'admin_view_business.html',{'data':data})

def admin_view_higher_studies(request,id):
    try:
        user_det = MyCustomUser.objects.get(id = id)
        data = HigherStudiesStatus.objects.get(user = user_det)
    except:
        data = None 
        messages.success(request,'no data found')
        return redirect('/admin_view_user_detials/'+id+'/')
    return render(request,'admin_view_higher_studies.html',{'data':data})

def admin_view_other(request,id):
    try:
        user_det = MyCustomUser.objects.get(id = id)
        data = OtherStatus.objects.get(user = user_det)
    except:
        data = None 
        messages.success(request,'no data found')
        return redirect('/admin_view_user_detials/'+id+'/')
    return render(request,'admin_view_other.html',{'data':data})







def admin_view_user_detials(request,id):
    data = MyCustomUser.objects.filter(user_type = 'user',status = 'Accepted')
    return render(request,'admin_view_user_detials.html',{'data':data,'id':id})


def admin_user_list(request):
    data = MyCustomUser.objects.filter(user_type = 'user',status = 'Accepted')
    return render(request,'admin_user_list.html',{'data':data})


#-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

def add_or_update_other(request):
    # 
    try:
        other_status = OtherStatus.objects.get(user=request.user)
    except OtherStatus.DoesNotExist:
        other_status = None

    if request.method == 'POST':
        description = request.POST.get('description')
        is_active = request.POST.get('is_active') == 'on'

        if other_status:  
            other_status.description = description
            other_status.is_active = is_active
            other_status.save()
            messages.success(request, 'Other status has been updated successfully.')
        else:  
            new_other_status = OtherStatus.objects.create(
                user=request.user,
                description=description,
                is_active=is_active
            )
            messages.success(request, 'Other status has been added successfully.')

        return redirect('/add_or_update_other')  

    context = {
        'other_status': other_status
    }
    return render(request, 'add_or_update_other.html', context)

#-----------------------------------------------------------------------------------------------------------------------------------------------------------------------



def add_or_update_marital_status(request):
    
    try:
        marital_status = MaritalStatus.objects.get(user=request.user)
    except MaritalStatus.DoesNotExist:
        marital_status = None

    if request.method == 'POST':
        status = request.POST.get('status')
        spouse_name = request.POST.get('spouse_name')
        marriage_date = request.POST.get('marriage_date')

        if marital_status:  
            marital_status.status = status
            marital_status.spouse_name = spouse_name if status == 'married' else None
            marital_status.marriage_date = marriage_date if status == 'married' else None
            marital_status.save()
            messages.success(request, 'Marital status has been updated successfully.')
        else:  
            new_status = MaritalStatus.objects.create(
                user=request.user,
                status=status,
                spouse_name=spouse_name if status == 'married' else None,
                marriage_date=marriage_date if status == 'married' else None
            )
            messages.success(request, 'Marital status has been added successfully.')

        return redirect('/add_or_update_marital_status')  

    context = {
        'marital_status': marital_status
    }
    return render(request, 'add_or_update_marital_status.html', context)


#-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


def add_or_update_startup(request):
    try:
        startup = StartupStatus.objects.get(user=request.user)
    except StartupStatus.DoesNotExist:
        startup = None
    if request.method == 'POST':
        startup_name = request.POST.get('startup_name')
        description = request.POST.get('description')
        year_started = request.POST.get('year_started')
        is_active = request.POST.get('is_active') == 'on'

        if startup:  
            startup.startup_name = startup_name
            startup.description = description
            startup.year_started = year_started
            startup.is_active = is_active
            startup.save()
            messages.success(request, 'Startup details have been updated successfully.')
        else:  
            new_startup = StartupStatus.objects.create(
                user=request.user,
                startup_name=startup_name,
                description=description,
                year_started=year_started,
                is_active=is_active
            )
            messages.success(request, 'Startup details have been added successfully.')

        return redirect('/add_or_update_startup') 

    context = {
        'startup': startup
    }
    return render(request, 'add_or_update_startup.html', context)


#--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


def add_or_update_business(request):
    #
    try:
        business = BusinessStatus.objects.get(user=request.user)
    except BusinessStatus.DoesNotExist:
        business = None

    if request.method == 'POST':
        business_name = request.POST.get('business_name')
        business_type = request.POST.get('business_type')
        year_started = request.POST.get('year_started')
        description = request.POST.get('description')
        is_active = request.POST.get('is_active') == 'on'

        if business:  
            business.business_name = business_name
            business.business_type = business_type
            business.year_started = year_started
            business.description = description
            business.is_active = is_active
            business.save()
            messages.success(request, 'Business details have been updated successfully.')
        else:  
            new_business = BusinessStatus.objects.create(
                user=request.user,
                business_name=business_name,
                business_type=business_type,
                year_started=year_started,
                description=description,
                is_active=is_active
            )
            messages.success(request, 'Business details have been added successfully.')

        return redirect('/add_or_update_business')  

    context = {
        'business': business
    }
    return render(request, 'add_or_update_business.html', context)


#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


def add_or_update_higher_studies(request):
    
    try:
        higher_studies = HigherStudiesStatus.objects.get(user=request.user)
    except HigherStudiesStatus.DoesNotExist:
        higher_studies = None

    if request.method == 'POST':
        degree = request.POST.get('degree')
        institution_name = request.POST.get('institution_name')
        start_year = request.POST.get('start_year')
        end_year = request.POST.get('end_year') if request.POST.get('end_year') else None
        is_completed = request.POST.get('is_completed') == 'on'

        if higher_studies:  
            higher_studies.degree = degree
            higher_studies.institution_name = institution_name
            higher_studies.start_year = start_year
            higher_studies.end_year = end_year
            higher_studies.is_completed = is_completed
            higher_studies.save()
            messages.success(request, 'Higher studies details have been updated successfully.')
        else:  
            new_higher_studies = HigherStudiesStatus.objects.create(
                user=request.user,
                degree=degree,
                institution_name=institution_name,
                start_year=start_year,
                end_year=end_year,
                is_completed=is_completed
            )
            messages.success(request, 'Higher studies details have been added successfully.')

        return redirect('/add_or_update_higher_studies')  

    context = {
        'higher_studies': higher_studies
    }
    return render(request, 'add_or_update_higher_studies.html', context)





#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





def edit_profile(request):
    user = MyCustomUser.objects.get(username = request.user)  
    if request.method == 'POST':
        user.username = request.POST['username']
        user.email = request.POST['email']
        user.dob = request.POST['dob']
        user.age = request.POST['age']
        user.gender = request.POST['gender']
        user.course = request.POST['course']
        user.department = request.POST['department']
        user.save()

        messages.success(request, 'Profile updated successfully.')
        return redirect('/edit_profile')

    context = {
        'user': MyCustomUser.objects.get(username = request.user)
    }
    return render(request, 'edit_profile.html', context)


#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



def admin_handle_request(request, reg_id, action):
    registration = get_object_or_404(MyCustomUser, id=reg_id)
    
    if action == 'accept':
        registration.status = 'Accepted'
        registration.save()

    elif action == 'decline':
        registration.status = 'decline'
        registration.save()
    
    return redirect('/admin_view_request')

#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


def admin_view_request(request):
    pending_requests = MyCustomUser.objects.filter(status='Pending',user_type = 'user')
    return render(request, 'admin_view_request.html', {'data': pending_requests})

#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

def user_dashboard(request):
    return render(request,'user_dashboard.html')



#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

def admin_dashboard(request):
    return render(request,'admin_dashboard.html')

#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

def index(request):
    return render(request,'index.html')


#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


def admin_signup(request):
    if request.method == 'POST':
        username = request.POST.get('uname')
        password = request.POST.get('password')
        pin = request.POST.get('pin')

        if MyCustomUser.objects.filter(username=username).exists():
            error_message = 'Username is already taken. Please choose another one.'
            messages.error(request, error_message)

        if pin != '1234':
            error_message = 'Wrong Pin.'
            messages.error(request, error_message)
            return render(request, 'admin_signup.html', {'error_message': error_message})
        if MyCustomUser.objects.filter(user_type = 'admin').exists():
            error_message = 'Admin is already exist.'
            messages.error(request, error_message)
            return redirect('/login')
        if len(password) <= 2:
            error_message = 'Use 2 or more characters in your password.'
            messages.error(request, error_message)
            return render(request, 'admin_signup.html', {'error_message': error_message})
       
        user = MyCustomUser.objects.create_user(username=username, password=password,user_type='admin')
        messages.success(request, 'Signup successful! You can now log in.')
        return redirect('/login')

    return render(request, 'admin_signup.html')


#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


def user_signup(request):
    if request.method == 'POST':
        full_name = request.POST.get('uname')
        password = request.POST.get('password')
        email = request.POST.get('email')
        dob = request.POST.get('dob')
        age = request.POST.get('age')
        gender = request.POST.get('genter')
        course = request.POST.get('course')
        department = request.POST.get('dept')
        confirm_password = request.POST.get('coonfirmpassword')

        # Check if username or email already exists
        if MyCustomUser.objects.filter(email=email).exists():
            error_message = 'Email is already registered. Please use another one.'
            messages.error(request, error_message)
            return render(request, 'user_signup.html', {'error_message': error_message})

        # Password validation
        if len(password) < 3:
            error_message = 'Password must contain at least 3 characters.'
            messages.error(request, error_message)
            return render(request, 'user_signup.html', {'error_message': error_message})

        if password != confirm_password:
            error_message = 'Passwords do not match. Please try again.'
            messages.error(request, error_message)
            return render(request, 'user_signup.html', {'error_message': error_message})

        # Create new user and save to database
        user = MyCustomUser.objects.create_user(
            username=full_name,
            password=password,
            email=email,
            dob=dob,
            age=age,
            gender=gender,
            course=course,
            department=department,
            user_type='user'  # Assuming you're categorizing users by type
        )

        messages.success(request, 'Signup successful! You can now log in.')
        return redirect('/login')

    return render(request, 'user_signup.html')



#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


def loginn(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        user = auth_authenticate(request, username=username, password=password)
        print(password)
        print(username)

        
        if user:
            data = MyCustomUser.objects.get(username = username)
       
        if user is not None:
            if data.user_type == 'user':
                auth_login(request, user)
                if data.status == 'Pending':
                    messages.success(request, 'Admin has not accepted yet.')
                    return redirect('/login')
                elif data.status == 'decline':
                    messages.success(request, 'Admin has rejected your account.')
                    return redirect('/login')
                else:
                    return redirect('/user_dashboard')

            elif data.user_type == 'admin':
                auth_login(request, user)
                return redirect('/admin_dashboard')
        else:          
            return render(request, 'login.html', {'error_message': 'Invalid username or password'})
    return render(request,'login.html')

#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

def logout(request):
    log(request)
    return redirect('/')
