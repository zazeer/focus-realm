import 'package:flutter/material.dart';
import 'package:frontend/API/userapiauth.dart';
import 'package:frontend/themes/app_colors.dart';
import 'package:flutter_svg/flutter_svg.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({Key? key}) : super(key: key);

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  bool _isLoading = false;

  @override
  void dispose() {
    _usernameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _register() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });

      try {
        // Buat user model dari input
        final user = UserModel(
          username: _usernameController.text.trim(),
          email: _emailController.text.trim(),
          password: _passwordController.text
        );

        // Panggil API register
        final response = await UserApiService.registerUser(user);

        if (response.errorCode == "200") {
          // Register berhasil
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Successfully registered!'),
              backgroundColor: Colors.green,
            ),
          );
          // Kembali ke login page
          Navigator.pop(context);
        } else if (response.errorCode == "204" && response.errorMessage == "Username already exists"){
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Username already exists!, Choose Another Username'),
              backgroundColor: Colors.red,
            ),
          );
        } else if (response.errorCode == "204" && response.errorMessage == "Email already exists") {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Email already exists!, Choose Another Email'),
              backgroundColor: Colors.red,
            ),
          );
        } else {
          // Register gagal
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(response.errorMessage ?? 'Registration failed'),
              backgroundColor: Colors.red,
            ),
          );
        }
      } catch (e) {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('An error occurred: $e'),
            backgroundColor: Colors.red,
          ),
        );
      } finally {
        if (mounted) {
          setState(() {
            _isLoading = false;
          });
        }
      }
    }
  }
  // @override
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
       body: Column(
        children: [
          _buildHeader(),
          // Bungkus widget form card dengan Expanded
          Expanded(
            child: Container(
              color: AppColors.background, 
              child: Transform.translate(
                offset: const Offset(0, -50),
                child: _buildFormCard(),
              ),
            ),
          ),
        ],
      ),
    );
  }

  /// Widget untuk header biru melengkung di bagian atas
  Widget _buildHeader() {
    return ClipPath(
      // clipper: _HeaderClipper(),
      child: Container(
        height: 200,
        color: AppColors.primary,
        child: const Center(
          child: Text(
            'FOCUSREALM',
            style: TextStyle(
              fontFamily: 'Poppins', // Ganti dengan font Anda
              color: AppColors.background,
              fontSize: 36,
              fontWeight: FontWeight.bold,
              letterSpacing: 2,
            ),
          ),
        ),
      ),
    );
  }

  /// Widget untuk kartu yang berisi semua elemen form
  Widget _buildFormCard() {
    return Container(
      decoration: BoxDecoration(
        color: AppColors.background,
        // Ubah borderRadius agar hanya dua sudut atas yang melengkung
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(40),
          topRight: Radius.circular(40),
        ),
      ),
      child: SingleChildScrollView(

        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 32),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Sign Up',
                style: TextStyle(
                  color: AppColors.primary,
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 24),
              _buildTextField(
                controller: _usernameController,
                hint: 'Username',
                icon: Icons.person_outline,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Username is required";
                  }
                  if (value.length < 3) {
                    return 'Username must be at least 3 characters';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
              _buildTextField(
                controller: _emailController,
                hint: 'Email',
                icon: Icons.email_outlined,
                keyboardType: TextInputType.emailAddress,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Email is required";
                  }
                  if (!RegExp(r'\S+@\S+\.\S+').hasMatch(value)) {
                    return 'Please enter a valid email address';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
              _buildTextField(
                controller: _passwordController,
                hint: 'Password',
                icon: Icons.lock_outline,
                isPassword: true,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Password is required";
                  }
                  if (value.length < 6) {
                    return 'Password must be at least 6 characters';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 32),
              _buildSubmitButton(),
              const SizedBox(height: 24),
              _buildSocialLoginDivider(),
              const SizedBox(height: 24),
              _buildSocialLoginButtons(),
              const SizedBox(height: 32),
              _buildLoginRedirect(),
            ],
          ),
        ),
      ),
    );
  }  

  /// Widget modular untuk text field
  Widget _buildTextField({
    required TextEditingController controller,
    required String hint,
    required IconData icon,
    bool isPassword = false,
    TextInputType? keyboardType,
    String? Function(String?)? validator,
  }) {
    return TextFormField(
      controller: controller,
      obscureText: isPassword,
      keyboardType: keyboardType,
      decoration: InputDecoration(
        hintText: hint,
        prefixIcon: Icon(icon, color: AppColors.light),
        filled: true,
        fillColor: AppColors.background,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: AppColors.primary),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: Colors.red),
        ),
      ),
      validator: validator,
    );
  }
  
  /// Widget untuk tombol Sign Up dengan gradien
  Widget _buildSubmitButton() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [AppColors.primary, AppColors.secondary],
          begin: Alignment.centerLeft,
          end: Alignment.centerRight,
        ),
        borderRadius: BorderRadius.circular(12),
      ),
      child: ElevatedButton(
        onPressed: _isLoading ? null : _register,
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.transparent,
          shadowColor: Colors.transparent,
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        child: _isLoading
            ? const SizedBox(
                height: 24,
                width: 24,
                child: CircularProgressIndicator(color: AppColors.background, strokeWidth: 3),
              )
            : const Text(
                'Sign Up',
                style: TextStyle(
                  color: AppColors.background,
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
      ),
    );
  }

  /// Widget untuk pemisah "Or sign up with"
  Widget _buildSocialLoginDivider() {
    return const Row(
      children: [
        Expanded(child: Divider(color: AppColors.light)),
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 8.0),
          child: Text(
            'Or sign up with',
            style: TextStyle(color: AppColors.light),
          ),
        ),
        Expanded(child: Divider(color: AppColors.light)),
      ],
    );
  }

  /// Widget untuk tombol-tombol social login
  Widget _buildSocialLoginButtons() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        _socialButton('assets/images/google-icon.svg'),
        const SizedBox(width: 20),
        _socialButton('assets/images/facebook-icon.svg'),
        const SizedBox(width: 20),
        _socialButton('assets/images/x-icon.svg'),
      ],
    );
  }
  
  Widget _socialButton(String assetPath) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppColors.background,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.grey.shade300),
      ),
      child: SvgPicture.asset(
        assetPath,
        height: 24,
        width: 24,
      ),
    );
  }

  /// Widget untuk teks dan tombol navigasi ke halaman Login
  Widget _buildLoginRedirect() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Text(
          'Already Have An Account? ',
          style: TextStyle(color: AppColors.light),
        ),
        GestureDetector(
          onTap: () => Navigator.pop(context),
          child: const Text(
            'Login',
            style: TextStyle(
              color: AppColors.primary,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ],
    );
  }
}

